;GeoServer Null Soft Installer creation file.
;Written by Chris Holmes

;We will eventually make this buildable from ant, but for a first
;stab the way to use this is to build a complete binary release with
;ant.  Then unzip that file in a fresh directory and copy this nsi file
;to the geoserver/ directory created by that unzip.  Then compile the
;nsi script from there.  If you have any questions just email 
;geoserver-devel@lists.sourceforge.net, and we can write up a better guide and/or
;get things working right in ant.

;--------------------------------
;Include Modern UI

  !include "MUI.nsh"

;--------------------------------
;General

  ;Name and file
  Name "GeoServer 1.3.0-beta"
  OutFile "geoserver-1.3.0-beta.exe"


  ;Default installation folder
  InstallDir "$PROGRAMFILES\GeoServer 1.3"
  
  ;Get installation folder from registry if available
  InstallDirRegKey HKCU "Software\GeoServer-1.3" ""

;--------------------------------
;Variables

  Var MUI_TEMP
  Var STARTMENU_FOLDER

;--------------------------------
;Interface Settings

  !define MUI_ICON   "${NSISDIR}\Contrib\Graphics\Icons\win-install.ico"
  !define MUI_UNICON "${NSISDIR}\Contrib\Graphics\Icons\win-uninstall.ico"
  
  !define MUI_ABORTWARNING
  !define MUI_WELCOMEPAGE_TEXT "This wizard will guide you through the \
      installation of GeoServer 1.3 \r\n \r\nNote that this is the first \
      attempt by the GeoServer project to create \
      a Windows executable installer.  \
      Please report any problems or suggestions for improvement to \
      geoserver-devel@lists.sourceforge.net. \r\n \r\n \
      Click Next to continue."
;--------------------------------
;Pages

  !insertmacro MUI_PAGE_WELCOME
  Page custom echoJava
  !insertmacro MUI_PAGE_LICENSE "license.txt"
  !insertmacro MUI_PAGE_DIRECTORY

  
  ;Start Menu Folder Page Configuration
  !define MUI_STARTMENUPAGE_REGISTRY_ROOT "HKCU" 
  !define MUI_STARTMENUPAGE_REGISTRY_KEY "Software\GeoServer 1.3" 
  !define MUI_STARTMENUPAGE_REGISTRY_VALUENAME "Start Menu Folder"
  
  !insertmacro MUI_PAGE_STARTMENU Application $STARTMENU_FOLDER
  
  !insertmacro MUI_PAGE_INSTFILES

  !insertmacro MUI_PAGE_FINISH
  
  !insertmacro MUI_UNPAGE_WELCOME
  !insertmacro MUI_UNPAGE_CONFIRM
  !insertmacro MUI_UNPAGE_INSTFILES
  !insertmacro MUI_UNPAGE_FINISH

;--------------------------------
;Languages
 
  !insertmacro MUI_LANGUAGE "English"

;--------------------------------
;Installer Sections

Section "GeoServer Section" SecGeoServer

  SetOutPath "$INSTDIR"
  
  ;ADD YOUR OWN FILES HERE...
  File /r bin
  File /r documents
  File /r server
  File /a README.txt
  File /r lib
  File /a RUNNING.txt
  File /a license.txt

  ;Store installation folderh
  WriteRegStr HKCU "Software\GeoServer" "" $INSTDIR
  
  ;Create uninstaller
  WriteUninstaller "$INSTDIR\Uninstall.exe"
  
  !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
    
    ;Create shortcuts
    Call findJavaPath
    Pop $2


    CreateDirectory "$SMPROGRAMS\$STARTMENU_FOLDER"
    SetOutPath "$INSTDIR\bin"
    CreateShortCut "$SMPROGRAMS\$STARTMENU_FOLDER\GeoServer Documentation.lnk"\
                   "$INSTDIR\documents\index.html"
    CreateShortCut "$SMPROGRAMS\$STARTMENU_FOLDER\GeoServer Administration.lnk" \
                   "http://127.0.0.1:8080/geoserver/"

    CreateShortCut "$SMPROGRAMS\$STARTMENU_FOLDER\Start GeoServer.lnk" \
                   "$2\bin\java.exe" '-jar start.jar'\
                   "$INSTDIR\server\geoserver\images\gs.ico" 0 SW_SHOWNORMAL
    CreateShortCut "$SMPROGRAMS\$STARTMENU_FOLDER\Stop GeoServer.lnk" \
                   "$2\bin\java.exe" '-jar stop.jar'\
                   "$INSTDIR\server\geoserver\images\gs.ico" 0 SW_SHOWMINIMIZED
    CreateShortCut "$SMPROGRAMS\$STARTMENU_FOLDER\Uninstall.lnk" "$INSTDIR\Uninstall.exe"

  
  !insertmacro MUI_STARTMENU_WRITE_END

SectionEnd

;--------------------------------
;Descriptions

  ;Language strings
  LangString DESC_GeoServer ${LANG_ENGLISH} "GeoServer section"

  ;Assign language strings to sections
  !insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
    !insertmacro MUI_DESCRIPTION_TEXT ${SecGeoServer} $(DESC_GeoServer)
  !insertmacro MUI_FUNCTION_DESCRIPTION_END

Function echoJava

   ClearErrors

   Call findJavaPath
   Pop $1
   MessageBox MB_OK "Using Java Development Kit found in $1.  If you would \
     like GeoServer to use another JDK then hit Cancel and set the JAVA_HOME \ 
     environment variable the location of your preferred JDK.  If your JDK \
     is not version 1.4 then please download and install a new one from \ 
     http://java.sun.com/j2se/1.4.  NOTE: this must be JDK/SDK - not a JRE."

FunctionEnd

Function .onInit

   ClearErrors

   Call findJavaPath

FunctionEnd

; =====================
; FindJavaPath Function
; =====================
;
; Find the JAVA_HOME used on the system, and put the result on the top of the
; stack
; Will exit if the path cannot be determined
;
Function findJavaPath

  ClearErrors

  ReadEnvStr $1 JAVA_HOME

  IfErrors 0 FoundJDK

  ClearErrors

  ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Development Kit" "CurrentVersion"
  ReadRegStr $1 HKLM "SOFTWARE\JavaSoft\Java Development Kit\$2" "JavaHome"
  ReadRegStr $3 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
  ReadRegStr $4 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$3" "RuntimeLib"

  FoundJDK:

  IfErrors 0 NoAbort
    MessageBox MB_OK "Couldn't find a Java Development Kit installed on this \
    computer. Please download one from http://java.sun.com/j2se/1.4. If there \
    is already a JDK 1.4 or greater installed on this computer, set an \
    environment variable JAVA_HOME to the pathname of the directory where it \
    is installed.   NOTE: this must be JDK/SDK - not a JRE."

    Abort

  NoAbort:

  ; Put the result in the stack
  Push $1

FunctionEnd



;--------------------------------
;Uninstaller Section

Section "Uninstall"

  ;ADD YOUR OWN FILES HERE...
  
  Delete "$INSTDIR\Uninstall.exe"
  RMDIR /r "$INSTDIR\bin"
  RMDIR /r "$INSTDIR\documents"
  RMDIR /r "$INSTDIR\server"
  RMDIR /r "$INSTDIR\lib"
  Delete "$INSTDIR\*.txt"

  RMDir "$INSTDIR"
  
  IfFileExists "$INSTDIR" 0 Removed
     MessageBox MB_YESNO|MB_ICONQUESTION \
          "Remove all files in your GeoServer 1.3 directory? (If you have anything you created that you want to keep, click No)" IDNO Removed
     Delete "$INSTDIR\*.*" ;
     RMDIR /r "$INSTDIR"
     Sleep 500
     IfFileExists "$INSTDIR" 0 Removed
        MessageBox MB_OK|MB_ICONEXCLAMATION \
            "Note: $INSTDIR could not be removed."
  Removed:


  !insertmacro MUI_STARTMENU_GETFOLDER Application $MUI_TEMP
    
  Delete "$SMPROGRAMS\$MUI_TEMP\Start Geoserver.lnk"
  Delete "$SMPROGRAMS\$MUI_TEMP\Stop Geoserver.lnk"
  Delete "$SMPROGRAMS\$MUI_TEMP\Uninstall.lnk"
  Delete "$SMPROGRAMS\$MUI_TEMP\GeoServer Documentation.lnk"
  Delete "$SMPROGRAMS\$MUI_TEMP\GeoServer Administration.lnk"
  
  ;Delete empty start menu parent diretories
  StrCpy $MUI_TEMP "$SMPROGRAMS\$MUI_TEMP"
 
  startMenuDeleteLoop:
    RMDir $MUI_TEMP
    GetFullPathName $MUI_TEMP "$MUI_TEMP\.."
    
    IfErrors startMenuDeleteLoopDone
  
    StrCmp $MUI_TEMP $SMPROGRAMS startMenuDeleteLoopDone startMenuDeleteLoop
  startMenuDeleteLoopDone:

  DeleteRegKey /ifempty HKCU "Software\GeoServer-1.3"

SectionEnd