#!usr/bin/python
from socket import *
import math
from random import randint
host = 'localhost' # '127.0.0.1' can also be used
port = 52000

index,isT,noL,noT,command = -1,-1,-1,-1,-1

sock = socket()
sock.connect((host, port)) #Connect takes tuple of host and port


# #Infinite loop to keep client running.
# message = raw_input("-> ")

# while message!="q":
while 1:
    data = sock.recv(1024)
    print data
    dataArr = data.split()
    keyWord = dataArr[0]

    if keyWord == "TRAITOR":
        isT = 1
    if keyWord == "INDEX":
        index = dataArr[1]
    if keyWord == "COMMAND":
        noL = int(dataArr[1])
        noT = int(dataArr[2])
        command = int(dataArr[4])
        # path = dataArr[3]
        # pathLen = len(dataArr[3])
        # for i in range(noT - pathLen):
        for j in range(noL):
            if j == index:
                pass
            else:
                if isT:
                    sock.send("INPUT C|%s %s %s" %(index,randint(0,1),j))
                else:
                    sock.send("INPUT C|%s %s %s" %(index,command,j))
        print(dataArr)


    if keyWord == "INPUT":
        print(dataArr)

sock.close()















# import socket
#
# def main():
#     host = "127.0.0.1"
#     port = 5000
#
#     s = socket.socket()
#     s.connect((host, port))
#
#     message = raw_input("-> ")
#
#     while message!="q":
#         s.sendall(message)
#         data = s.recv(1024)
#
#         print "Received from Server: "+ str(data)
#
#         message = raw_input("-> ")
#
#     s.close()
#
# if __name__ == "__main__":
#     main()
