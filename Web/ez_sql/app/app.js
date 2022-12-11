import { Application, Router, helpers } from "https://deno.land/x/oak/mod.ts";
import Flight from './db.js';

const app = new Application();
const router = new Router();

router.get('/', async(ctx) => {
    ctx.response.body = 'check your flight `/flight?id=`';
});

router.get('/flight', async(ctx) => {
    const id = helpers.getQuery(ctx, { mergeParams: true });
    const info = await Flight.select({departure: 'departure', destination: 'destination'}).where(id).all();
    ctx.response.body = info;
});

app.use(router.routes());
app.use(router.allowedMethods());

app.listen({ port: 3000, hostname: '0.0.0.0' });
