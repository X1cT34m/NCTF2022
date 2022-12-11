import { DataTypes, Database, Model, SQLite3Connector} from "https://deno.land/x/denodb@v1.0.40/mod.ts";

const connector = new SQLite3Connector({
    filepath: '/tmp/flight.db'
});

const db = new Database(connector);

class Flight extends Model {
    static table = 'flight';
  
    static fields = {
      id: { primaryKey: true, autoIncrement: true },
      departure: DataTypes.STRING,
      destination: DataTypes.STRING,
    };
}

class Flag extends Model {
    static table = 'flag';

    static fields = {
        flag: DataTypes.STRING,
    };
}

db.link([Flight, Flag]);

await db.sync({ drop: true });

await Flight.create({
    departure: 'Paris',
    destination: 'Tokyo',
});

await Flight.create({
    departure: 'Las Vegas',
    destination: 'Washington',
});

await Flight.create({
    departure: 'London',
    destination: 'San Francisco',
});

await Flag.create({
    flag: Deno.env.get('flag'),
});

export default Flight
