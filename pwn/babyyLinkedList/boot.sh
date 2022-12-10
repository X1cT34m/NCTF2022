#!/bin/sh
qemu-system-x86_64 \
  -m 256M \
  -kernel ./bzImage \
  -initrd ./rootfs.cpio \
  -append "root=/dev/ram rw console=ttyS0 oops=panic panic=1 kaslr pti=on quiet" \
  -cpu kvm64,+smep \
  -net user -net nic -device e1000 \
  -smp cores=2,threads=2 \
  -monitor /dev/null \
  -nographic \
  -s
