## Setting up MySQL Container

```bash
docker run -d -p 3306:3306 --name mysql-docker-container -e MYSQL_ROOT_PASSWORD=spring -e MYSQL_DATABASE=spring -e MYSQL_USER=spring -e MYSQL_PASSWORD=spring mysql/mysql-server:latest
```

## Spring Batch

**Step types:**
- Tasklet step - a simple step like send SMS EMAIL etc ...
- Chunk oriented step - CSV with millions of lines, we have to create chunks so we dont get out of memory exception

**Steps:**
- Item Reader - Some kind of input, CSV 
- Item processor - An ItemProcessor is used to process the data. When the given item is not valid it returns null, else it processes the given item and returns the processed result
- Item Writer - Some kind of output, MySQL 

![Architecture](https://raw.githubusercontent.com/matebence/spring/batch/docs/architecture.png)

- Every job runs only once (if nothing changed)
- The change can be triggered via Arguments parameters like (run=one)
- If we want to run same parameter multiple times we have to use .incrementer(new RunIdIncrementer())

**Job instance and executation**

Job Instance - it runs on 01 March

Job execution - it runs on 02 am

`It it was successfull it can not run multiple times`

Job Instance - it runs on 02 March

Job execution - it runs on 03 am

`We can run it again if it failed`

**Job execution context**
- Every step can access the values from the context
- Save to the db at the end

**Step execution context**
- Each step has its own execution context, which is then saved into the db (on each step)
- The status of the steps and their order
