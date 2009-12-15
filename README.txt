
To depoy if credentials are expired during an ant update:

make sure the java\bin dir is in your path

for windoze
%GAE_HOME%\bin\appcfg.cmd update C:\workspace\swagswap\target\swagswap

for mac/linux
$GAE_HOME/bin/appcfg.sh update /Users/sambrodkin/Documents/workspace/swagswap/target/swagswap/

To deploy through a firewall:
%GAE_HOME%\bin\appcfg.cmd --proxy=172.23.100.53:8080 update d:\workspaces\gae-workspace\swagswap\target\swagswap-0.1
