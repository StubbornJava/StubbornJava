FROM mysql:5.7

COPY mysqld.cnf /etc/mysql/mysql.conf.d/mysqld.cnf
COPY ./setup.sh /docker-entrypoint-initdb.d/setup.sh

EXPOSE 3306

CMD ["mysqld"]
