# Project

Используемая база данных - postgresql.
Для подключения необходимо
создать базу данных используя скрип создания базы из файла script_create_db.txt 
поменять в классе ConnectDB
String USER = "postgres";
String PASS = "1234";
на значения логина и пароля суперпользователя бд postgresql
В URL = "jdbc:postgresql://localhost:5432/HotelDataBases?autoReconnect=true&useSSL=false";
заменить HotelDataBases на название созданной базы.

