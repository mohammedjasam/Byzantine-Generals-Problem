# Import all from module socket
from socket import *
#Importing all from thread
from thread import *
import math
from random import randint


# Defining server address and port
host = ''  #'localhost' or '127.0.0.1' or '' are all same
port = 52000 #Use port > 1024, below it all are reserved



# print "Enter"
print "# of L, # of T, isTraitor?, Command\n"
noL,noT,isT,command = 3,2,1,1


ran = []  ## List to store the Random Traitors
for i in range(noT-isT):
    while True:
        x = randint(0,noL-1)
        if x not in ran:
            ran.append(x)
            break



#Creating socket object
sock = socket()
#Binding socket to a address. bind() takes tuple of host and port.
sock.bind((host, port))
#Listening at the address
sock.listen(noL) #5 denotes the number of clients can queue

def clientthread(conn):

     while True:
         data = conn.recv(1024) # 1024 stands for bytes of data to be received
         print data

         dataArr = data.split()
         keyWord = dataArr[0]

         if keyWord == "INPUT":
             path = dataArr[1]
             cmd = dataArr[2]
             child = connList[int(dataArr[3])]
             child.send("INPUT %s %s" %(path, cmd))

         if keyWord == "OUTPUT":
             cmd = dataArr[1];
             child = connList[int(dataArr[2])]
             child.send("OUTPUT %s" %(cmd))


connList = []
while True:
    conn, addr = sock.accept()
    connList.append(conn)  ## List to store the conn objects
    start_new_thread(clientthread,(conn,)) #start new thread takes 1st argument as a function name to be run, second is the tuple of arguments to the function.
    print "Connected to: %s" %(len(connList)-1)
    if len(connList)==noL:
        break

print "first"
for x in range(noL):
    connList[x].send("INDEX %s"%x)
    print x

print "second"
for i in ran:
    connList[i].send("TRAITOR %s"%i)
    print i

#starting algorithm
print "third"
if isT:
    for x in range(noL):
        connList[x].send("COMMAND %s %s C %s" %(noL, noT, randint(0,1)))
else:
    for x in range(noL):
        connList[x].send("COMMAND %s %s C %s" %(noL, noT, command))

while 1:
    a=1



# conn.close()
# sock.close()


# import socket
#
#
# def main():
#     # Defining the Host address and the port at which we want to communicate
#     host = ""
#     port = 5000
#
#     s = socket.socket()
#
#     # binding the socket to the host and port
#     s.bind((host, port))
#
#     # We listen to one Client at a time!
#     s.listen(7)
#     c, addr = s.accept()
#     print "Connection From: " + str(addr)
#     while True:
#         data  =  c.recv(10)
#         if not data:
#             break
#         print "From connected user: " + str(data)
#         data = str(data).upper()
#         print "Sending: " + str(data)
#         c.send(data)
#
#     c.close()
# if __name__ == "__main__":
#     main()
