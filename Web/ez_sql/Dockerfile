FROM denoland/deno:latest

COPY app/ /app/

WORKDIR /app

ENV flag flag{54fee2fae48a7ea9eaff7483f7038c9e}

RUN deno cache --import-map import_map.json app.js

CMD deno run --allow-write --allow-read --allow-env --allow-net --import-map import_map.json app.js
