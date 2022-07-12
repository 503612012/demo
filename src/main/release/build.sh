echo 'begin build demo image...'
docker build -t demo:1.0.0 ./
echo 'build demo image finish...'

flag=$(cat /var/spool/cron/root | grep demo | grep -v grep | awk '{print $6}')
if [ "$flag" == '' ]; then
  echo 'begin set database backup schedule...'
  echo '0 */6 * * * /home/demo/backup.sh' >> /var/spool/cron/root
  systemctl restart crond
  echo 'set database backup schedule finish...'
else
  echo "the cron has already config~"
fi