# Auth microservice which is used for logging in and getting auth token

import psycopg2

conn = psycopg2.connect(dbname="postgres", user="postgres", password="12345678", host="192.168.1.122", port="5433")
cur = conn.cursor()
cur.execute("CREATE TABLE test3 (id serial PRIMARY KEY, num integer, data varchar);")
cur.execute("INSERT INTO test3 (num, data) VALUES (%s, %s)",
            (100, "abc'def"))
cur.execute("SELECT * FROM test3;")
print(cur.fetchone())
conn.commit()
cur.close()
conn.close()