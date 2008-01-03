CITE Testing Instructions
-------------------------

A. Get Engine Sources

Create a directory called 'engine', and check the out the teamengine sources
into it.

  'mkdir engine'
  'svn co http://teamengine.svn.sourceforge.net/svnroot/teamengine/trunk
engine'
  
B. Get Test Sources

Create a directory called 'tests', and check out the cite test sources into
it.

  'mkdir tests'
  'svn co https://svn.opengeospatial.org:8443/ogc-projects/cite/trunk tests'

*Note* : You will need a user name and password for the cite subversion
 repository. If you have a cite portal account, that will work.

C. Build the Test Engine

The test engine is built with the following command: 

  'mvn clean install'

D. Run the Test Suite

Running a test suite is done with the command:

  'run.sh <profile>'

Where profile is one of the following:

  'wfs-1.0.0'
  'wfs-1.1.0'
  'wms-1.1.1'
  'wcs-1.0.0'

Examples:

  'run.sh wfs-1.0.0'

E. View the Test Logs

Viewing the entire log of a test run is done with the command:

  'log.sh <profile>'

Viewing the log of a single test is done with the command:

  'log.sh <profile> <testid>'

Where testid is the identifer for the test. For example:

  'log.sh wfs-1.0.0 wfs-1.0.0/w24aac25b3b9d185b1_1'

F. Note for Windows Users

At this time, run.sh and log.sh have yet to ported to batch files. Windows
users must use the batch files included with the test engine. 

The equivalent to 'run.sh <profile>':

  'engine/bin/test.sh 
      -logdir=target/logs
      -source=tests/<service>/<version>/ets/ctl/[main.xml|<service>.xml] 
      -session=<profile>' 

Where service and version are the service and version being tested
respectively. Examples:

  'engine/bin/test.sh
      -logdir=target/logs
      -source=tests/wfs/1.0.0/ets/ctl/wfs.xml
      -session=wfs-1.0.0'

  'engine/bin/test.sh
      -logdir=target/logs
      -source=tests/wfs/1.1.0/ets/ctl/main.xml
      -session=wfs-1.1.0'

The equivalent to 'log.sh <profile> [<test>]':

  'engine/bin/viewlog.sh -logdir=target/logs -session=<profile> [<test>]'

Example:

  'engine/bin/viewlog.sh -logdir=target/logs -session=wfs-1.0.0 wfs-1.0.0/w24aac25b3b9d185b1_1' 


