if [ "$1" = "" ]; then
  echo "Usage: $0 <profile>"
  exit -1
fi

#parse out service and version
service=${1:0:3}
version=${1:4:$#1}

#find the control file
ctl=""
if [ -e tests/$service/$version/ets/ctl/main.xml ]; then
  ctl=main.xml
else 
  if [ -e tests/$service/$version/ets/ctl/main.ctl ]; then
    ctl=main.ctl
  else 
    if [ -e tests/$service/$version/ets/ctl/$service.xml ]; then
      ctl=$service.xml
    fi
  fi
fi

if [ "$ctl" = "" ]; then
  echo "Error: could not find control file 'main.xml' or '$service.xml' under 'tests/$service/$version/ets/ctl/'"
  exit -1
fi
ctl=tests/$service/$version/ets/ctl/$ctl

mode=test
if [ "$2" != "" ]; then
  if [ ! -e target/logs/$1 ]; then
    echo "Error: No logs found for profile '$1'."
    exit -1
  fi
  mode=retest
else
  if [ -e target/logs/$1 ]; then
    mode=resume
  fi
fi

if [ "$mode" = "resume" ]; then
  sh engine/bin/test.sh -mode=$mode -source=$ctl -logdir=target/logs/ -session=$1
else 
  sh engine/bin/test.sh -mode=$mode -source=$ctl -logdir=target/logs/ -session=$1 $2
fi
