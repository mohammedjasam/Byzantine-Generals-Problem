import socket


def main():
    # Defining the Host address and the port at which we want to communicate
    host = "127.0.0.1"
    port = 5000

    s = socket.socket()

    # binding the socket to the host and port
    s.bind((host, port))

    # We listen to one Client at a time!
    s.listen(1)
    c, addr = s.accept()
    print("Connection From: " + str(addr))
    while True:
        data  =  c.recv(1024)
        if not data:
            break
        print("From connected user: " + str(data))
        data = str(data).upper()
        print("Sending: " + str(data))
        c.send(data)

    c.close()
if __name__ == "__main__":
    main()
