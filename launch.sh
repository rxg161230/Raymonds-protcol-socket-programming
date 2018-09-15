remoteuser=car021000
remotecomputer1=dc30.utdallas.edu
remotecomputer2=dc31.utdallas.edu
remotecomputer3=dc32.utdallas.edu
remotecomputer4=dc34.utdallas.edu
#remotecomputer5=dc35.utdallas.edu
#remotecomputer6=dc36.utdallas.edu
#remotecomputer7=dc37.utdallas.edu
#remotecomputer8=dc38.utdallas.edu
#remotecomputer9=dc39.utdallas.edu
#remotecomputer10=dc40.utdallas.edu
#remotecomputer11=dc41.utdallas.edu
#remotecomputer12=dc42.utdallas.edu
#remotecomputer13=dc43.utdallas.edu
#remotecomputer14=dc44.utdallas.edu
#remotecomputer15=dc45.utdallas.edu

ssh -l "$remoteuser" "$remotecomputer1" "cd $HOME/Stuff/Project2/project2/src;java Node 0 n" &
ssh -l "$remoteuser" "$remotecomputer2" "cd $HOME/Stuff/Project2/project2/src;java Node 1 n"
ssh -l "$remoteuser" "$remotecomputer3" "cd $HOME/Stuff/Project2/project2/src;java Node 2 n" &
ssh -l "$remoteuser" "$remotecomputer4" "cd $HOME/Stuff/Project2/project2/src;java Node 3 n" &
#ssh -l "$remoteuser" "$remotecomputer5" "cd $HOME/Stuff/Project2/project2/src;java Node 4 n" &
#ssh -l "$remoteuser" "$remotecomputer6" "cd $HOME/Stuff/Project2/project2/src;java Node 5 n" &
#ssh -l "$remoteuser" "$remotecomputer7" "cd $HOME/Stuff/Project2/project2/src;java Node 6 n" &
#ssh -l "$remoteuser" "$remotecomputer8" "cd $HOME/Stuff/Project2/project2/src;java Node 7 n" &
#ssh -l "$remoteuser" "$remotecomputer9" "cd $HOME/Stuff/Project2/project2/src;java Node 8 n" &
#ssh -l "$remoteuser" "$remotecomputer10" "cd $HOME/Stuff/Project2/project2/src;java Node 9 n" &
#ssh -l "$remoteuser" "$remotecomputer11" "cd $HOME/Stuff/Project2/project2/src;java Node 10 n" &
#ssh -l "$remoteuser" "$remotecomputer12" "cd $HOME/Stuff/Project2/project2/src;java Node 11 n" &
#ssh -l "$remoteuser" "$remotecomputer13" "cd $HOME/Stuff/Project2/project2/src;java Node 12 n" &
#ssh -l "$remoteuser" "$remotecomputer14" "cd $HOME/Stuff/Project2/project2/src;java Node 13 n" &
#ssh -l "$remoteuser" "$remotecomputer15" "cd $HOME/Stuff/Project2/project2/src;java Node 14 n" &



#Run this script on CS machine.
#Prerequisite - Passwordless login should be enabled using Public keys and you should have logged on to the net machines atleast once after creating a public key.
#example
#-bash-4.1$ ssh net23.utdallas.edu
#The authenticity of host 'net23.utdallas.edu (10.176.67.86)' can't be established.
#RSA key fingerprint is 66:af:c1:ce:29:b8:5b:7b:8e:25:33:92:bb:96:0e:46.
#Are you sure you want to continue connecting (yes/no)? yes

#Your code should be in directory $HOME/AOS/Project1
#Your main program should be named Project1.java or Project1.cpp or Project1.c
