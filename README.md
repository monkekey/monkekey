#修改环境变量
*ENV_ACTIVE = prod
*DOCKERHOST=$(ifconfig | grep -E "([0-9]{1,3}\.){3}[0-9]{1,3}" | grep -v 127.0.0.1 | awk '{ print $2 }' | cut -f2 -d: | head -n1)


#修改主机路由规则， 容器访问主机mysql
>mysql -uroot -p 
>use mysql;
>upate user set host = '%' where host='localhost' and user = 'root';
>flush privileges;

iptables -A INPUT -i docker0 -j ACCEPT


#nodejs
>curl --silent --location https://rpm.nodesource.com/setup_6.x | sudo bash -
>sudo yum -y install nodejs
>npm install -g wscat
>wscat -c ws://127.0.0.1:5544/ws

#创建基础docker image
Doc -> silk-v3-decoder

#silk  mp3转换
需要gcc和ffmpeg。gcc是拿来编译silk v3 decoder源码，ffmpeg是拿来转换格式的
>https://github.com/kn007/silk-v3-decoder
>http://yurixu.com/blog/2016/09/02/FFmpeg%E9%9F%B3%E8%A7%86%E9%A2%91%E8%BD%AC%E6%8D%A2/

#编译并执行
sh converter.sh 171023055026574913455.silk mp3

#silk ==> pcm
silk/decoder test.silk test.pcm
#pcm  ==> mp3
ffmpeg -y -f s16le -ar 24000 -ac 1 -i test.pcm test.mp3




UPDATE sysuserdetail a
INNER JOIN sysuser b 
ON a.uid = b.id
SET a.faces = b.account
WHERE a.faces is null;

update sysuserdetail set `Faces` = concat(concat(Inncode, concat("/",faces)), '.png')



lf13579
