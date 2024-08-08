<<<<<<< HEAD
#!/bin/bash
# Search for existence of uid in user file
grep -w "$1" user_sample.csv

=======
#search for existance of uid in user file
searchresult=$(grep -w $1 user-store.txt)
echo $searchresult
>>>>>>> origin/main
