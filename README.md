# Instructions to run

1. Create following directory tree on your %HOMEPATH
   - *data/in* <br/>
   - *data/out* <br/>
   - *data/processed* <br/>
2. Run tests and build the project: <br/>
   - *mvn clean package*
      - **Important: This step will delete all content from _data/in_, _data/out_ and _data/processed_ directories**
3. Start the system:
   - *java -jar target/stats-watcher-api-0.0.1.jar*
4. Add files to be processed to *data/in* directory
5. After finishing, the output report is created at *data/out* and the input file is moved to *data/processed*
