# bookstore
 Bookstore management


The bookstore management project loads the data from the given csv file into an in-memory H2 database. While inserting records, it calculates the vector for the book views. This approach holds good only for a limited set of records. For an infinitely long list of book view documents, I would prefer indexing them to Elasticsearch on startup so that reading will be quicker while startup might take a little longer.

Once the records are inserted to the database, a LoadingCache is used to store the list of suggestions for a given book. This ensures that the read time is less for subsequent requests to the same document.
