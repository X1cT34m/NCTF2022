import socketserver
from Crypto.Util.number import *
from Crypto.Cipher import AES
import os
import signal
import string
import hashlib
from random import *
banner = br"""
 __        __   _                            _          _   _  ____ _____ _____ ____   ___ ____  ____  
 \ \      / /__| | ___ ___  _ __ ___   ___  | |_ ___   | \ | |/ ___|_   _|  ___|___ \ / _ \___ \|___ \ 
  \ \ /\ / / _ \ |/ __/ _ \| '_ ` _ \ / _ \ | __/ _ \  |  \| | |     | | | |_    __) | | | |__) | __) |
   \ V  V /  __/ | (_| (_) | | | | | |  __/ | || (_) | | |\  | |___  | | |  _|  / __/| |_| / __/ / __/ 
    \_/\_/ \___|_|\___\___/|_| |_| |_|\___|  \__\___/  |_| \_|\____| |_| |_|   |_____|\___/_____|_____|
                                                                                                       
"""

class Task(socketserver.BaseRequestHandler):
    def encode(self):
        message = open('message.txt', 'r').read()
        key = os.urandom(16)
        IV = os.urandom(16)
        message = [message[i*32:(i+1)*32] for i in range(len(message) // 32 + 1)]
        cipher = b""
        for msg in message:
            aes = AES.new(key, AES.MODE_OFB, IV)
            cipher += aes.encrypt(msg.encode())
        return cipher.hex()

    def send(self, msg, newline=True):
        if newline:
            msg += b"\n"
        self.request.sendall(msg)
    def _recvall(self):
        BUFF_SIZE = 2048
        data = b''
        while True:
            part = self.request.recv(BUFF_SIZE)
            data += part
            if len(part) < BUFF_SIZE:
                break
        return data.strip()
    def recv(self, prompt=b'> '):
        self.send(prompt, newline=False)
        return self._recvall()

    def handle(self):
        signal.alarm(1200)
        self.send(banner)
        self.send(b"This is your cipher")
        c = self.encode()
        self.send(c.encode())
        self.send(b"Plz tell me my the md5(message):")
        md5 = self.recv()
        if md5.decode() == hashlib.md5(open('message.txt', 'rb').read()).hexdigest():
            self.send(b"Congratulation!\nHere is your flag")
            self.send(open('flag.txt', 'rb').read())


class ThreadedServer(socketserver.ThreadingMixIn, socketserver.TCPServer):
    pass


class ForkedServer(socketserver.ForkingMixIn, socketserver.TCPServer):
    pass


if __name__ == "__main__":
    HOST, PORT = '0.0.0.0', 10001
    while 1:
        try:
            server = ForkedServer((HOST, PORT), Task)
            server.allow_reuse_address = True
            print("Server at 0.0.0.0 port "+str(PORT))
            server.serve_forever()
        except:
            PORT = PORT+1 
