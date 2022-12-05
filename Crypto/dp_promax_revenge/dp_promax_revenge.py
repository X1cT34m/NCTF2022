from Crypto.Util.number import *
from random import *
import socketserver
import signal
from hashlib import sha256
from string import ascii_letters, digits

table = ascii_letters + digits


class Task(socketserver.BaseRequestHandler):
    def _recvall(self):
        BUFF_SIZE = 2048
        data = b''
        while True:
            part = self.request.recv(BUFF_SIZE)
            data += part
            if len(part) < BUFF_SIZE:
                break
        return data.strip()

    def send(self, msg, newline=True):
        try:
            if newline:
                msg += b'\n'
            self.request.sendall(msg)
        except:
            pass

    def recv(self, prompt=b'[-] '):
        self.send(prompt, newline=False)
        return self._recvall()

    def proof_of_work(self):
        proof = (''.join([choice(table) for _ in range(12)])).encode()
        sha = sha256(proof).hexdigest().encode()
        self.send(b"[+] sha256(XXXX+" + proof[4:] + b") == " + sha)
        XXXX = self.recv(prompt=b'[+] Plz Tell Me XXXX :')
        if len(XXXX) != 4 or sha256(XXXX + proof[4:]).hexdigest().encode() != sha:
            return False
        return True

    def handle(self):
        signal.alarm(120)
        if not self.proof_of_work():
            self.send(b'Oops,you are wrong. Bye~')
            self.request.close()
        self.send(b'NOW! You are prohibited from using factordb!')
        self.send(b'Someone says\nTHOSE WHO SUBMIT THE FACTORIZATION TO FACTOR.DB WEBSITE')
        self.send(b'During the competition\nNaive!!!')
        nbits = 2200
        beta = 0.4090
        p, q = getPrime(int((1. - beta) * nbits)), getPrime(int(beta * nbits))
        dp = getrandbits(50) | 1
        while True:
            d = (p - 1) * getrandbits(2800) + dp
            if GCD((p - 1) * (q - 1), d) == 1:
                break
        e = inverse(d, (p - 1) * (q - 1))
        N = p * q
        self.send(str(e).encode())
        self.send(str(N).encode())
        self.send(b'Tell me q:')
        check = int(self.recv())
        if check == q:
            flag = open('flag', 'rb').read()
            self.send(flag)
        else:
            self.send(b'Naive!')
        self.request.close()


class ThreadedServer(socketserver.ThreadingMixIn, socketserver.TCPServer):
    pass


class ForkedServer(socketserver.ForkingMixIn, socketserver.TCPServer):
    pass


if __name__ == "__main__":
    HOST, PORT = '0.0.0.0', 5001

    print("HOST:POST " + HOST + ":" + str(PORT))
    server = ForkedServer((HOST, PORT), Task)
    server.allow_reuse_address = True
    server.serve_forever()
