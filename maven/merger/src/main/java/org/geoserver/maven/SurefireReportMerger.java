package org.geoserver.maven;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * @goal mergeSurefireReports
 * @description Merges surefire reports into the execution root target directory. 
 */
public class SurefireReportMerger extends AbstractMojo {

	/**
     * The Maven project running this plugin.
     *
     * @parameter expression="${project}"
     * @required
     */
    private MavenProject project;
    
	public void execute() throws MojoExecutionException, MojoFailureException {
		
		File source = 
			new File( project.getBuild().getDirectory(), "surefire-reports" );
		if ( !source.exists() ) {
			getLog().info( "No surefire reports to merge" );
			return;
		}
		
		//find the root
		MavenProject root = project;
		while( !root.isExecutionRoot() && root.hasParent()) {
			root = root.getParent();
		}
		
		if ( !root.isExecutionRoot() ) {
			String msg = "Could not locate execution root";
			throw new MojoExecutionException( msg );
		}
		
		//make the target reports directory
		File target = 
			new File( root.getBuild().getDirectory(), "surefire-reports" );
		if ( !target.exists() ) {
			target.mkdirs();
		}
		
		//copy files to target
		File[] files = source.listFiles(
			new FileFilter() {
				public boolean accept(File pathname) {
					return pathname.getName().startsWith( "TEST-" ) && 
						pathname.getName().endsWith( ".xml" );
				}
			}
		);
		
		for ( int i = 0; i < files.length; i++ ) {
			try {
				getLog().info( "Copying " + files[i].getName() + " to " + target.getAbsolutePath() );
				copy( files[i], target );
			} 
			catch (IOException e) {
				getLog().warn( "Unable to copy: " + files[i].getName() );
				getLog().warn( e );
			}
		}
		
	}

	void copy( File file, File target ) throws IOException {
		File dest = new File( target, file.getName() );
		dest.createNewFile();
		
		InputStream in = 
			new BufferedInputStream( new FileInputStream( file ) );
		OutputStream out = 
			new BufferedOutputStream( new FileOutputStream( dest ) );
	
		int b = 0;
		while ( ( b = in.read() ) != -1 ) {
			out.write( b );
		}
		
		out.flush();
		out.close();
		in.close();
	}
}
