#!usr/bin/python
from socket import *

host = 'localhost' # '127.0.0.1' can also be used
port = 52000

sock = socket()
#Connecting to socket
sock.connect((host, port)) #Connect takes tuple of host and port

#Infinite loop to keep client running.
while True:
    data = sock.recv(1024)
    print data
    sock.send('HI! I am client.')

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
