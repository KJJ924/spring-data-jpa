## Master Slave 


1. compose docker

```bash
docker-compose up -d
```


2. docker master network ip address paste 
```bash
dokcer network ls
docker inspect {CONTAINER ID}
```


3. Approach slave container
```bash
docker ps
docker exec -it {SLAVE_CONTAINER_ID} bash
mysql -u root -p
```

4. slave setting
```bash
stop slave;

CHANGE MASTER TO 
MASTER_HOST='{master network ip address}', 
MASTER_USER='root', 
MASTER_PASSWORD='password', 
MASTER_LOG_FILE='mysql-bin.000001', 
MASTER_LOG_POS=0, 
GET_MASTER_PUBLIC_KEY=1;

start slave;

show slave status\G;

```

5. check status   

![image](https://user-images.githubusercontent.com/64793712/126068024-feaeef47-d7be-4060-87cd-ff192bb61a6f.png)
