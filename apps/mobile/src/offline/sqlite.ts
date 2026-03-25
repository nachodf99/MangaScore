import * as SQLite from 'expo-sqlite';

export const db = SQLite.openDatabaseSync('mangafish.db');

export function bootstrapLocalDb() {
  db.execSync(`
    create table if not exists local_catches (
      id text primary key,
      client_uuid text not null unique,
      manga_id text not null,
      participant_id text not null,
      payload text not null,
      sync_status text not null default 'pending',
      local_created_at text not null,
      local_updated_at text not null,
      last_error text
    );
  `);
}
