#return line number of specific search term
searchresult=$(grep -n $1 user-store.txt | awk -F: '{print $2}')
echo $searchresult

