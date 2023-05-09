INSERT INTO person (id,age,first_name,last_name)
SELECT 99,30,'Foo','Baz' WHERE NOT EXISTS(SELECT * FROM person WHERE id=99);