#!/bin/bash

# Các thông tin về cơ sở dữ liệu
DB_CONTAINER_NAME=postgres-1
DB_USER=user
DB_NAME=trendify
BACKUP_PATH=/Users/tienvi/Development/UIT/backup

# Tên file backup với timestamp
BACKUP_FILE=$BACKUP_PATH/db_backup$(date +\%Y-\%m-\%d).backup

# Thực hiện lệnh pg_dump bên trong container Docker
docker exec -t $DB_CONTAINER_NAME pg_dump -U $DB_USER -F c -b -v -f $BACKUP_FILE $DB_NAME

# Kiểm tra và xoá các file backup cũ hơn 7 ngày
find $BACKUP_PATH -type f -name "*.backup" -mtime +7 -exec rm {} \;
