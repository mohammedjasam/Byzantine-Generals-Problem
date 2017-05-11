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
print "# of L, # of T, isTraitor?, Command\n")
noL,noT,isT,command = 6,2,1,1


ran = []  ## List to store the Random Traitors
for i in range(noT-isT):
    while True:
        x = randint(0,noL)
        if x not in ran:
            ran.append()
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


connList = []
while True:
    conn, addr = sock.accept()
    connList.append(conn)  ## List to store the conn objects
    start_new_thread(clientthread,(conn,'Hi! I am server\n')) #start new thread takes 1st argument as a function name to be run, second is the tuple of arguments to the function.
    if len(connList)==noL:
        break

for i in ran:
    connList[i].send("TRAITOR")




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
