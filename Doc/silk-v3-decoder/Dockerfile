FROM java:8-jre
MAINTAINER tdz <teddyzhou@126.com>

RUN apt-get update && \
    apt-get -y install gcc wget make g++ openssl libssl-dev libpcre3 libpcre3-dev zlib1g zlib1g-dev tar bzip2 automake autoconf libtool vim

RUN cd /home && wget http://cfhcable.dl.sourceforge.net/project/lame/lame/3.99/lame-3.99.5.tar.gz && tar -zxvf lame-3.99.5.tar.gz && cd lame-3.99.5 && ./configure --enable-shared && make && make install

RUN cd /home && wget http://ffmpeg.org/releases/ffmpeg-3.4.tar.bz2 && tar -jxvf ffmpeg-3.4.tar.bz2 && cd ffmpeg-3.4 && ./configure --enable-shared --disable-yasm --enable-libmp3lame && make && make install

ADD ./libc.conf 	/etc/ld.so.conf.d/
RUN ldconfig


ADD ./silk-v3-decoder-master	  /home/silk-v3-decoder/


