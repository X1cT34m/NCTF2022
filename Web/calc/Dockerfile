FROM python:3.8.10

RUN pip3 install flask
RUN rm -rf /bin/sh && ln -s /bin/bash /bin/sh
RUN echo flag > /Th1s_is__F1114g
COPY ./calc /home
EXPOSE 5000


