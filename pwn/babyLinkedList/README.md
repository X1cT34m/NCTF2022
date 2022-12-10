# NCTF2022 - babyLinkedList

baby challenge in musl-libc, have fun

Here are some remote environment that you may need

1、the flag is in `/home/ctf/flag`

2、part of `dockerfile`:

```dockerfile
RUN chown -R root:ctf /home/ctf && \
    chmod -R 750 /home/ctf && \
    patchelf --set-interpreter /home/ctf/libc.so /home/ctf/babyLinkedList
```

