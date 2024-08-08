<<<<<<< HEAD
#!/bin/bash
# Add record to users file
echo "$1, $2, $3, $4, $5, $6, $7, $8, $9, ${10}, ${11}, ${12}" >> user_sample.csv
echo 0
=======
#add reord to users file
adduser=$(echo "$1    $2   $3    $4    $5    $6   $7    $8    $9    ${10}    ${11}    ${12}" >> user-store.txt)
echo $?
>>>>>>> origin/main
