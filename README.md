# Instructions to run
1. Create following directory tree on your %HOMEPATH
   - *data/in* <br/>
   - *data/out* <br/>
   - *data/processed* <br/>
2. From project root run the following commands:
   - *mvn clean package*
   - *java -jar target/stats-watcher-api-0.0.1.jar*
3. Add the files to be processed to *data/in* directory
4. After finishing, the output report is created at *data/out* and the input file is moved to *data/processed*
