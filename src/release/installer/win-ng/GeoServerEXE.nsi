; GeoServer Windows installer creation file.

; Define your application name
!define APPNAME "GeoServer"
!define APPNAMEANDVERSION "GeoServer 2.0.1"

; Main Install settings
Name "${APPNAMEANDVERSION}"
InstallDir "$PROGRAMFILES\${APPNAMEANDVERSION}"
InstallDirRegKey HKLM "Software\${APPNAME}" ""
OutFile "geoserver-2.0.1-ng.exe"

; Modern interface settings
!include "MUI.nsh"

!include "StrFunc.nsh"

; Might be the same as !define
Var "JavaHome"
Var "DataDir"
Var "DataNewOrUsed"
Var STARTMENU_FOLDER
	
; Install options page headers
LangString TEXT_JDK_TITLE ${LANG_ENGLISH} "Java Development Kit"
LangString TEXT_JDK_SUBTITLE ${LANG_ENGLISH} "Java Development Kit path selection"
LangString TEXT_DATADIR_TITLE ${LANG_ENGLISH} "GeoServer Data Directory"
LangString TEXT_DATADIR_SUBTITLE ${LANG_ENGLISH} "GeoServer Data Directory path selection"
LangString TEXT_READY_TITLE ${LANG_ENGLISH} "Ready to Install"
LangString TEXT_READY_SUBTITLE ${LANG_ENGLISH} "GeoServer is ready to be installed"
LangString TEXT_CREDS_TITLE ${LANG_ENGLISH} "GeoServer Administrator"
LangString TEXT_CREDS_SUBTITLE ${LANG_ENGLISH} "Set administrator credentials"

;Interface Settings
!define MUI_ICON "gs.ico"
!define MUI_UNICON "${NSISDIR}\Contrib\Graphics\Icons\win-uninstall.ico"
!define MUI_HEADERIMAGE
!define MUI_HEADERIMAGE_RIGHT
!define MUI_HEADERIMAGE_BITMAP header.bmp
!define MUI_WELCOMEFINISHPAGE_BITMAP side_left.bmp

;Start Menu Folder Page Configuration
!define MUI_STARTMENUPAGE_REGISTRY_ROOT "HKLM" 
!define MUI_STARTMENUPAGE_REGISTRY_KEY "Software\${APPNAME}" 
!define MUI_STARTMENUPAGE_REGISTRY_VALUENAME "Start Menu Folder"

; "Are you sure you wish to cancel" popup.
!define MUI_ABORTWARNING

; Optional welcome text here
  !define MUI_WELCOMEPAGE_TEXT "This wizard will guide you through the installation of GeoServer 2.0.1. \r\n\r\n\
	It is recommended that you close all other applications before starting Setup.\
	This will make it possible to update relevant system files without having to reboot your computer.\r\n\r\n\
	Please report any problems or suggestions to the GeoServer Users mailing list: geoserver-users@lists.sourceforge.net. \r\n\r\n\
	Click Next to continue."

; What to do when done
; !define MUI_FINISHPAGE_RUN "$INSTDIR\wrapper.exe -t ./bin/wrapper/wrapper.conf"

; Install Page order
; This is the main list of installer things to do 
!insertmacro MUI_PAGE_WELCOME                                 ; Hello
Page custom CheckUserType                                     ; Die if not admin
!insertmacro MUI_PAGE_LICENSE "LICENSE.txt"                   ; Show license
!insertmacro MUI_PAGE_DIRECTORY                               ; Where to install
!insertmacro MUI_PAGE_STARTMENU Application $STARTMENU_FOLDER ; Start menu location
Page custom JavaCheck                                         ; Check for JDK
Page custom DataDirCheck                                      ; Set data directory
Page custom CredsCheck                                        ; Will set admin/password (if new install)
Page custom Ready                                             ; Ready to install page
!insertmacro MUI_PAGE_INSTFILES                               ; Actually do the install
!insertmacro MUI_PAGE_FINISH                                  ; Done

; Uninstall Page order
!insertmacro MUI_UNPAGE_CONFIRM   ; Are you sure you wish to uninstall?
!insertmacro MUI_UNPAGE_INSTFILES ; Do the uninstall
!insertmacro MUI_UNPAGE_FINISH    ; Done

; Set languages (first is default language)
!insertmacro MUI_LANGUAGE "English"
!insertmacro MUI_RESERVEFILE_LANGDLL


; Startup tasks
Function .onInit
	
; Splash screen
  SetOutPath $TEMP
  File /oname=spltmp.bmp "splash.bmp"
  advsplash::show 1500 500 0 -1 $TEMP\spltmp
	;advsplash::show Delay FadeIn FadeOut KeyColor FileName
  Pop $0 ; $0 has '1' if the user closed the splash screen early,
         ;    has '0' if everything closed normally, and '-1' if some error occurred.
  Delete $TEMP\spltmp.bmp
	
; Extract install options from .ini files
  !insertmacro MUI_INSTALLOPTIONS_EXTRACT "jdk.ini"
  !insertmacro MUI_INSTALLOPTIONS_EXTRACT "datadir.ini"
  !insertmacro MUI_INSTALLOPTIONS_EXTRACT "creds.ini"
  !insertmacro MUI_INSTALLOPTIONS_EXTRACT "ready.ini"
		
FunctionEnd


; Check the user type, and quit if it's not an administrator.
; Taken from Examples/UserInfo that ships with NSIS.
Function CheckUserType
  ClearErrors
  UserInfo::GetName
  IfErrors Win9x
  Pop $0
  UserInfo::GetAccountType
  Pop $1
  StrCmp $1 "Admin" Admin NoAdmin

  NoAdmin:
    MessageBox MB_ICONSTOP "Sorry, you must have administrative rights in order to install GeoServer"
    Quit

  Win9x:
    MessageBox MB_ICONSTOP "This installer is not supported on Windows 9x/ME."
    Quit
		
  Admin:
	
FunctionEnd


; Find the %JAVA_HOME% used on the system, and put the result on the top of the stack
; Will return an empty string if the path cannot be determined
Function findJavaPath

  ClearErrors

  ReadEnvStr $1 JAVA_HOME

  IfErrors 0 FoundJDK

  ClearErrors
  ; Commented out becasue it appears to not distinguish between JDKs and JREs.
  ; ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
  ; ReadRegStr $1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$2" "JavaHome"
  ; ReadRegStr $3 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$2" "RuntimeLib"

  FoundJDK:
  IfErrors 0 NoErrors
  StrCpy $1 ""

  NoErrors:
  ClearErrors
  ; Put the result in the stack
  Push $1

FunctionEnd

; The page to specify the %JAVA_HOME% path. 
Function JavaCheck
  !insertmacro MUI_HEADER_TEXT "$(TEXT_JDK_TITLE)" "$(TEXT_JDK_SUBTITLE)"
  CheckAgain:
	Call findJavaPath
  Pop $3
	; This puts the JDK, if any, into the field...
  !insertmacro MUI_INSTALLOPTIONS_WRITE "jdk.ini" "Field 4" "State" $3
  !insertmacro MUI_INSTALLOPTIONS_DISPLAY "jdk.ini"
	
	; ...and reads it back in (may be redundant)
  !insertmacro MUI_INSTALLOPTIONS_READ $3 "jdk.ini" "Field 4" "State"
  IfFileExists "$3\bin\java.exe" NoErrors Errors
	
	Errors:
    MessageBox MB_RETRYCANCEL|MB_ICONSTOP "No Java Development Kit (JDK) found in folder:$\r$\n$3$\r$\nPress Retry to try again or Cancel to Exit." IDRETRY CheckAgain IDCANCEL Kill

  Kill:
    MessageBox MB_OK "Setup will now exit.  Please make sure you have a valid JDK on your system."
  Quit
	
  NoErrors:
    StrCpy "$JavaHome" $3

FunctionEnd


; Find the %GEOSERVER_DATA_DIR% used on the system, and put the result on the top of the stack
; Will return an empty string if the path cannot be determined
Function findDataDirPath

  ClearErrors
  ReadEnvStr $1 GEOSERVER_DATA_DIR
  IfErrors 0 NoErrors

  StrCpy $1 "$INSTDIR\data_dir"

  NoErrors:
    ClearErrors
    ; Put the result in the stack
    Push $1

FunctionEnd


Function DataDirCheck
	
	!insertmacro MUI_HEADER_TEXT "$(TEXT_DATADIR_TITLE)" "$(TEXT_DATADIR_SUBTITLE)"
	
  CheckDirAgain:
	Call findDataDirPath
  Pop $4
	; This puts the JDK, if any, into the field.
  !insertmacro MUI_INSTALLOPTIONS_WRITE "datadir.ini" "Field 2" "State" $4
  !insertmacro MUI_INSTALLOPTIONS_DISPLAY "datadir.ini"
	
	;...and reads it back in (may be redundant)
  !insertmacro MUI_INSTALLOPTIONS_READ $4 "datadir.ini" "Field 2" "State"
  IfFileExists "$4\catalog.xml" Exist Errors
		
  Errors:
	  StrCmp "$4" "$INSTDIR\data_dir" ErrorNewDir ErrorBadDir
	
	ErrorNewDir:
	  ; This is the dir that will be created, so it's okay that it doesn't exist.
	  ; MessageBox MB_OK "Setup will create a new data directory at the following path:$\r$\n$4"
		StrCpy $DataNewOrUsed "New"
		Goto NoErrors

  ErrorBadDir:
	  MessageBox MB_RETRYCANCEL|MB_ICONSTOP "No valid GeoServer data directory found in folder:$\r$\n$4$\r$\nPress Retry to try again or Cancel to Exit." IDRETRY CheckDirAgain IDCANCEL Kill

  Kill:
    MessageBox MB_OK "Setup will now exit."
  Quit

Goto CheckDirAgain
	
	Exist:
	  MessageBox MB_OK "Setup will use the existing data directory at:$\r$\n$4"
		StrCpy $DataNewOrUsed "Used"
	  Goto NoErrors
	
  NoErrors:
	StrCpy "$DataDir" $4
	
	; Writes environment variable
	Push GEOSERVER_DATA_DIR
  Push $DataDir
  Call WriteEnvStr
		
FunctionEnd

; Page for setting admin username/password
; Will be skipped if using a pre-existing data dir
Function CredsCheck

StrCmp $DataNewOrUsed "Used" SkipCreds SetCreds

  ; New data directory, so can set credentials without harming existing setup
  SetCreds:
	
	  !insertmacro MUI_HEADER_TEXT "$(TEXT_CREDS_TITLE)" "$(TEXT_CREDS_SUBTITLE)"
    !insertmacro MUI_INSTALLOPTIONS_DISPLAY "creds.ini"
		
		; Username (admin) to $6
	  !insertmacro MUI_INSTALLOPTIONS_READ $6 "creds.ini" "Field 3" "State"
    ; Password (geoserver) to $7
    !insertmacro MUI_INSTALLOPTIONS_READ $7 "creds.ini" "Field 5" "State"	

  SkipCreds:
    ; Skip this process since existing data dir already has credentials set
		
FunctionEnd

; One final page to review options
Function Ready

!insertmacro MUI_HEADER_TEXT "$(TEXT_READY_TITLE)" "$(TEXT_READY_SUBTITLE)"

; Big long string with all the settings
StrCpy $9 "Java Development Kit (JDK) directory:\r\n        $JavaHome\r\n\r\nGeoServer install directory:\r\n        $INSTDIR\r\n\r\nGeoServer data directory:\r\n        $DataDir"

!insertmacro MUI_INSTALLOPTIONS_WRITE "ready.ini" "Field 2" "Text" $9
!insertmacro MUI_INSTALLOPTIONS_DISPLAY "ready.ini"

FunctionEnd


; The main install section
Section "GeoServer" Main_Sec
	
	; Makes this install mandatory, although not necessary as it's the only section
	SectionIn RO

	; Set Section properties
	SetOverwrite on

	; Set Section Files and Shortcuts
	SetOutPath "$INSTDIR\"
	File /a start.jar
	File /a GPL.txt
	File /a LICENSE.txt
	File /a README.txt
	File /a RUNNING.txt
	File /a wrapper.exe
	File /a wrapper-server-license.txt
	File /r data_dir
	File /r bin
	File /r etc
	File /r lib
	File /r logs
	File /r resources
	File /r webapps

	
	StrCmp $DataNewOrUsed "New" WriteCreds NoWriteCreds
	
	; New users.properties file is created here
	WriteCreds:
		Delete "$DataDir\security\users.properties"
    FileOpen $R9 "$DataDir\security\users.properties" w
	  FileWrite $R9 "$6=$7,ROLE_ADMINISTRATOR"
    FileClose $R9
	
	NoWriteCreds:
	
	; Install the service
  nsExec::Exec "$INSTDIR\wrapper.exe -i ./bin/wrapper/wrapper.conf"


  !insertmacro MUI_STARTMENU_WRITE_BEGIN Application

    ;Create shortcuts
    CreateDirectory "$SMPROGRAMS\$STARTMENU_FOLDER"
    SetOutPath "$INSTDIR"
    CreateShortCut "$SMPROGRAMS\$STARTMENU_FOLDER\GeoServer Homepage.lnk" \
		               "http://geoserver.org"
    CreateShortCut "$SMPROGRAMS\$STARTMENU_FOLDER\GeoServer Web Admin Page.lnk" \
		               "http://localhost:8080/geoserver"
    CreateShortCut '$SMPROGRAMS\$STARTMENU_FOLDER\Start GeoServer.lnk' '"$INSTDIR\wrapper.exe"' '"-t" "bin/wrapper/wrapper.conf"' '$INSTDIR\webapps\geoserver\images\gs.ico' 0
		CreateShortCut '$SMPROGRAMS\$STARTMENU_FOLDER\Stop GeoServer.lnk'  '"$INSTDIR\wrapper.exe"' '"-p" "bin/wrapper/wrapper.conf"' '$INSTDIR\webapps\geoserver\images\gs.ico' 0
    CreateShortCut "$SMPROGRAMS\$STARTMENU_FOLDER\Uninstall.lnk" "$INSTDIR\Uninstall.exe"
	
  !insertmacro MUI_STARTMENU_WRITE_END
	
SectionEnd


; What happens at the end of the install.
Section -FinishSection

	WriteRegStr HKLM "Software\${APPNAME}" "" "$INSTDIR"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAMEANDVERSION}" "DisplayName" "${APPNAMEANDVERSION}"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAMEANDVERSION}" "UninstallString" "$INSTDIR\uninstall.exe"
	WriteUninstaller "$INSTDIR\uninstall.exe"

SectionEnd

; Commented out as there's only one install component
; Modern install component descriptions
; !insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
;	  !insertmacro MUI_DESCRIPTION_TEXT ${Main_Sec} "Installs Geoserver core components."
; !insertmacro MUI_FUNCTION_DESCRIPTION_END


; ----------------------------------
; Environment Variable stuff (start)

!ifndef _WriteEnvStr_nsh
  !define _WriteEnvStr_nsh
  !include WinMessages.nsh
 
  !ifndef WriteEnvStr_RegKey
    !ifdef ALL_USERS
      !define WriteEnvStr_RegKey \
       'HKLM "SYSTEM\CurrentControlSet\Control\Session Manager\Environment"'
    !else
			; This is hacked to set a System var, not a user var
      !define WriteEnvStr_RegKey  'HKLM "SYSTEM\CurrentControlSet\Control\Session Manager\Environment"'
    !endif
  !endif




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

; Environment Variable stuff (end)
; ----------------------------------


;Uninstall section
Section Uninstall

  Push GEOSERVER_DATA_DIR
  Call un.DeleteEnvStr

	;Remove from registry...
	DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAMEANDVERSION}"
	DeleteRegKey HKLM "SOFTWARE\${APPNAME}"

	; Delete self
	Delete "$INSTDIR\uninstall.exe"
	
	; Remove service
  nsExec::Exec "$INSTDIR\wrapper.exe -r ./bin/wrapper/wrapper.conf"


	; Delete Shortcuts
	RMDir /r "$SMPROGRAMS\${APPNAMEANDVERSION}"

	; Clean up GeoServer
	RMDir /r "$INSTDIR\"

SectionEnd

; This is the gray text on the bottom left of the installer.
; BrandingText "Where does this text go"

; eof
