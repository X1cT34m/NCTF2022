FROM php:5.6-apache

RUN docker-php-ext-install pdo pdo_mysql mysqli
RUN docker-php-ext-enable mysqli

RUN mv /etc/apt/sources.list /etc/apt/sources.list.bak
RUN echo "# flag{xxxxxxxxxxx}" >> /etc/passwd
RUN echo 'deb http://mirrors.163.com/debian/ stretch main non-free contrib' > /etc/apt/sources.list
RUN echo 'deb http://mirrors.163.com/debian/ stretch-updates main non-free contrib' >> /etc/apt/sources.list
RUN echo 'deb http://mirrors.163.com/debian-security/ stretch/updates main non-free contrib' >> /etc/apt/sources.list

RUN set -ex \
    && apt-get update -y \
    && apt-get install -y libapache2-mod-security2
    
COPY ./modsecurity/ /etc/modsecurity
COPY ./conf/000-default.conf /etc/apache2/sites-available/
COPY ./conf/security2.conf /etc/apache2/mods-available/

RUN service apache2 restart
