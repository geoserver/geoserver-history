;GeoServer NullSoft Installer creation file.
;Written by Chris Holmes
;
;We will eventually make this buildable from ant, but for a first
;stab the way to use this is to build a complete binary release with
;ant.  Then unzip that file in a fresh directory and copy this nsi file
;to the geoserver/ directory created by that unzip.  Then compile the
;nsi script from there.  If you have any questions just email 
;geoserver-devel@lists.sourceforge.net, and we can write up a better
;guide and/or get things working right in ant.
;
; 
;HOW TO USE THE NULLSOFT INSTALLER IS IN THE
;DOCUMENTATION/DEVELOPER/RELEASEGUIDE!!
;11:00 AM 5/23/2005
;
;Additional Comments/Edits made by Mike Pumphrey, May 2008

;--------------------------------
;Include Modern UI

  !include "MUI.nsh"
  XPStyle on

;--------------------------------
; Fix Vista installation problems
RequestExecutionLevel "admin"
  
;--------------------------------
;General

  ;Name and file
  Name "GeoServer 2.0.1"
  OutFile "geoserver-2.0.1.exe"

  ;Default installation folder
  InstallDir "$PROGRAMFILES\GeoServer 2.0.1"
  
  ;Get installation folder from registry if available
  InstallDirRegKey HKCU "Software\GeoServer-2.0.1" ""

;--------------------------------
;Variables
  
  Var MUI_TEMP
  Var STARTMENU_FOLDER
  Var DATA_DIR
  Var HWND

;--------------------------------
;Interface Settings

  !define MUI_ICON   "gs.ico"
  !define MUI_UNICON "${NSISDIR}\Contrib\Graphics\Icons\win-uninstall.ico"
  !define MUI_ABORTWARNING
  !define MUI_WELCOMEPAGE_TEXT "This wizard will guide you through the \
                                installation of GeoServer 2.0.1.  \
                                Please report any suggestions or issues \
								to geoserver-devel@lists.sourceforge.net. \r\n\
                                Click Next to continue."

;--------------------------------
;Pages

  !insertmacro MUI_PAGE_WELCOME
  Page custom echoJava
  !insertmacro MUI_PAGE_LICENSE "LICENSE.txt"
  !insertmacro MUI_PAGE_DIRECTORY

  Page custom dataDirPage dataDirPageLeave
  
  ;Start Menu Folder Page Configuration
  !define MUI_STARTMENUPAGE_REGISTRY_ROOT "HKCU" 
  !define MUI_STARTMENUPAGE_REGISTRY_KEY "Software\GeoServer" 
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

  ReserveFile "dataDirPage.ini"; -bo
  !insertmacro MUI_RESERVEFILE_INSTALLOPTIONS ; -bo

;--------------------------------
;Installer Sections

Section "GeoServer Section" SecGeoServer

  SetOutPath "$INSTDIR"
  
  ;ADD YOUR OWN FILES HERE...
  File /r bin
  File /r etc
  File /r data_dir
  File /a README.txt
  File /r lib
  File /r logs
  File /a RUNNING.txt
  File /a LICENSE.txt
  File /a start.jar
  File /r webapps
	
  ; Create the GEOSERVER_DATA_DIR environment variable
  ; (this will overwrite if one already exists)
  Push GEOSERVER_DATA_DIR
  Push $DATA_DIR
  Call WriteEnvStr

  ; Store installation folderh
  WriteRegStr HKCU "Software\GeoServer" "" $INSTDIR
  
  ;Create uninstaller
  WriteUninstaller "$INSTDIR\Uninstall.exe"
  
  !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
    
    ;Create shortcuts
    Call findJavaPath
    Pop $2

    CreateDirectory "$SMPROGRAMS\$STARTMENU_FOLDER"
    SetOutPath "$INSTDIR"
    CreateShortCut "$SMPROGRAMS\$STARTMENU_FOLDER\GeoServer Documentation.lnk"\
                   "http://geoserver.org"
    CreateShortCut "$SMPROGRAMS\$STARTMENU_FOLDER\GeoServer Administration.lnk" \
                   "http://localhost:8080/geoserver/"
    CreateShortCut "$SMPROGRAMS\$STARTMENU_FOLDER\Start GeoServer.lnk" \
                   "$2\bin\java.exe" '-DGEOSERVER_DATA_DIR="%GEOSERVER_DATA_DIR%" -Xmx300m -DSTOP.PORT=8079 -DSTOP.KEY=geoserver -jar start.jar'\
                   "$INSTDIR\webapps\geoserver\WEB-INF\images\gs.ico" 0 SW_SHOWNORMAL
    CreateShortCut "$SMPROGRAMS\$STARTMENU_FOLDER\Stop GeoServer.lnk" \
                   "$2\bin\java.exe" '-DSTOP.PORT=8079 -DSTOP.KEY=geoserver -jar start.jar --stop'\
                   "$INSTDIR\webapps\geoserver\WEB-INF\images\gs.ico" 0 SW_SHOWMINIMIZED
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
   MessageBox MB_OK "Using Java Development Kit (JDK) found in: \
     $\r $\n$1$\r $\n\
	 If you would like GeoServer to use another JDK then hit Cancel and set the \
	 JAVA_HOME environment variable the location of your preferred JDK. \
	 You can download a new one from \ 
     http://java.sun.com/javase/downloads. $\r\
	 NOTE: You must use a JDK - not a Java Runtime Environment (JRE)."

FunctionEnd

Function .onInit

   ;Splash Screen
   ;plugins dir is automatically deleted when the installer exits
   InitPluginsDir
   File /oname=$PLUGINSDIR\splash.bmp "splash.bmp"
   splash::show 1500 $PLUGINSDIR\splash
   ; $0 has '1' if the user closed the splash screen early,
   ; '0' if everything closed normally, and '-1' if some error occurred.
   Pop $0

   #Extract InstallOptions INI files
   !insertmacro MUI_INSTALLOPTIONS_EXTRACT "dataDirPage.ini"

   #load page link, save it to temp location
   WriteIniStr $PLUGINSDIR\dataDirPage.ini "Field 3" "State" "$WINDIR\Notepad.exe"
   
   ClearErrors

   Call findJavaPath
   
FunctionEnd


Function dataDirPage

  ;get the existing data dir environment variable
  ReadEnvStr $1 GEOSERVER_DATA_DIR
  ;MessageBox MB_OK "existing env string: $1"

  StrCmp $1 "" 0 copy_str
  ;if it doesn't exist, use: "$INSTDIR\data_dir"
  StrCpy $1 "$INSTDIR\data_dir"

  ;if it exists, use it for temp value until user chooses new one
  copy_str:
  StrCpy $DATA_DIR $1

  
  !insertmacro MUI_HEADER_TEXT "GeoServer Data Directory" "Choose the location of your data directory."
  !insertmacro MUI_INSTALLOPTIONS_INITDIALOG "dataDirPage.ini"
  Pop $HWND
  GetDlgItem $1 $HWND 1202	; 1200 + field number - 1 (MINUS ONE!!!!!!!! pos NSIS)
  SendMessage $1 ${WM_SETTEXT} 1 "STR:$DATA_DIR"
  !insertmacro MUI_INSTALLOPTIONS_SHOW

FunctionEnd


Function dataDirPageLeave
  
  !insertmacro MUI_INSTALLOPTIONS_READ $R1 "dataDirPage.ini" "Field 3" "State"
  StrCpy $DATA_DIR $R1
  ;MessageBox MB_OK "window value: $DATA_DIR"

FunctionEnd

;------------------------------------


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
    MessageBox MB_OK "Couldn't find a Java Development Kit (JDK) installed on this \
    machine. Please download one from http://java.sun.com/javase/downloads. \
	If there already is already a JDK (1.4 or newer) installed, please make sure the \
    JAVA_HOME environment variable is set correctly. \
    NOTE: You must have a JDK installed, not a Java Runtime Environment (JRE)."

    Abort

  NoAbort:

  ; Put the result in the stack
  Push $1

FunctionEnd


!ifndef _WriteEnvStr_nsh
  !define _WriteEnvStr_nsh
  !include WinMessages.nsh
 
  !ifndef WriteEnvStr_RegKey
    !ifdef ALL_USERS
      !define WriteEnvStr_RegKey \
       'HKLM "SYSTEM\CurrentControlSet\Control\Session Manager\Environment"'
    !else
      !define WriteEnvStr_RegKey 'HKCU "Environment"'
    !endif
  !endif
;close !endif?

;-----------------------------

# WriteEnvStr - Writes an environment variable
# Note: Win9x systems requires reboot
#
# Example:
#  Push "HOMEDIR"           # name
#  Push "C:\New Home Dir\"  # value
#  Call WriteEnvStr
Function WriteEnvStr
  Exch $1 ; $1 has environment variable value
  Exch
  Exch $0 ; $0 has environment variable name
  Push $2
 
  Call IsNT
  Pop $2
  StrCmp $2 1 WriteEnvStr_NT
    ; Not on NT
    StrCpy $2 $WINDIR 2 ; Copy drive of windows (c:)
    FileOpen $2 "$2\autoexec.bat" a
    FileSeek $2 0 END
    FileWrite $2 "$\r$\nSET $0=$1$\r$\n"
    FileClose $2
    SetRebootFlag true
    Goto WriteEnvStr_done
 
  WriteEnvStr_NT:
      WriteRegExpandStr ${WriteEnvStr_RegKey} $0 $1
      SendMessage ${HWND_BROADCAST} ${WM_WININICHANGE} \
        0 "STR:Environment" /TIMEOUT=5000
 
  WriteEnvStr_done:
    Pop $2
    Pop $0
    Pop $1
FunctionEnd

;---------------------------------------
 
# un.DeleteEnvStr - Removes an environment variable
# Note: Win9x systems requires reboot
#
# Example:
#  Push "HOMEDIR"           # name
#  Call un.DeleteEnvStr
Function un.DeleteEnvStr
  Exch $0 ; $0 now has the name of the variable
  Push $1
  Push $2
  Push $3
  Push $4
  Push $5
 
  Call un.IsNT
  Pop $1
  StrCmp $1 1 DeleteEnvStr_NT
    ; Not on NT
    StrCpy $1 $WINDIR 2
    FileOpen $1 "$1\autoexec.bat" r
    GetTempFileName $4
    FileOpen $2 $4 w
    StrCpy $0 "SET $0="
    SetRebootFlag true
 
    DeleteEnvStr_dosLoop:
      FileRead $1 $3
      StrLen $5 $0
      StrCpy $5 $3 $5
      StrCmp $5 $0 DeleteEnvStr_dosLoop
      StrCmp $5 "" DeleteEnvStr_dosLoopEnd
      FileWrite $2 $3
      Goto DeleteEnvStr_dosLoop
 
    DeleteEnvStr_dosLoopEnd:
      FileClose $2
      FileClose $1
      StrCpy $1 $WINDIR 2
      Delete "$1\autoexec.bat"
      CopyFiles /SILENT $4 "$1\autoexec.bat"
      Delete $4
      Goto DeleteEnvStr_done
 
  DeleteEnvStr_NT:
    DeleteRegValue ${WriteEnvStr_RegKey} $0
    SendMessage ${HWND_BROADCAST} ${WM_WININICHANGE} \
      0 "STR:Environment" /TIMEOUT=5000
 
  DeleteEnvStr_done:
    Pop $5
    Pop $4
    Pop $3
    Pop $2
    Pop $1
    Pop $0
FunctionEnd
 
!ifndef IsNT_KiCHiK
!define IsNT_KiCHiK
 
;---------------------------------------
 
# [un.]IsNT - Pushes 1 if running on NT, 0 if not
#
# Example:
#   Call IsNT
#   Pop $0
#   StrCmp $0 1 +3
#     MessageBox MB_OK "Not running on NT!"
#     Goto +2
#     MessageBox MB_OK "Running on NT!"
#
!macro IsNT UN
Function ${UN}IsNT
  Push $0
  ReadRegStr $0 HKLM \
    "SOFTWARE\Microsoft\Windows NT\CurrentVersion" CurrentVersion
  StrCmp $0 "" 0 IsNT_yes
  ; we are not NT.
  Pop $0
  Push 0
  Return
 
  IsNT_yes:
    ; NT!!!
    Pop $0
    Push 1
FunctionEnd
!macroend
!insertmacro IsNT ""
!insertmacro IsNT "un."
 
!endif ; IsNT_KiCHiK
 
!endif ; _WriteEnvStr_nsh 
 


;--------------------------------
;Uninstaller Section

Section "Uninstall"

  Push GEOSERVER_DATA_DIR
  Call un.DeleteEnvStr
  
  
  ;ADD YOUR OWN FILES HERE...
  
  Delete "$INSTDIR\Uninstall.exe"
  RMDIR /r "$INSTDIR\bin"
  RMDIR /r "$INSTDIR\etc"
  RMDIR /r "$INSTDIR\webapps"
  RMDIR /r "$INSTDIR\lib"
  Delete "$INSTDIR\*.txt"
  Delete "$INSTDIR\*.jar"

  RMDIR /r "$INSTDIR"
  
  IfFileExists "$INSTDIR" 0 Removed
     MessageBox MB_YESNO|MB_ICONQUESTION \
          "Remove all files in your GeoServer 2.0.1 directory? (If you have anything you created that you want to keep, click No)" IDNO Removed
     Delete "$INSTDIR\*.*"
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

  DeleteRegKey /ifempty HKCU "Software\GeoServer"

SectionEnd
