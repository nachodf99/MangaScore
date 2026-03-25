create extension if not exists "pgcrypto";

create type role_type as enum ('superadmin','admin_club','juez','participante');
create type catch_validation_status as enum ('pending','valid','rejected');
create type sync_status as enum ('pending','synced','error');

create table clubs (
  id uuid primary key default gen_random_uuid(),
  name text not null,
  region text not null default 'Andalucía',
  created_at timestamptz not null default now()
);

create table users (
  id uuid primary key default gen_random_uuid(),
  email text not null unique,
  full_name text not null,
  auth_provider_id text,
  created_at timestamptz not null default now()
);

create table memberships (
  id uuid primary key default gen_random_uuid(),
  club_id uuid not null references clubs(id) on delete cascade,
  user_id uuid not null references users(id) on delete cascade,
  role role_type not null,
  unique (club_id, user_id)
);

create table rulesets (
  id uuid primary key default gen_random_uuid(),
  club_id uuid not null references clubs(id) on delete cascade,
  name text not null,
  top_n int not null default 5 check(top_n between 1 and 20),
  weight_unit text not null default 'g' check(weight_unit in ('g','kg')),
  allow_length boolean not null default true,
  photo_required boolean not null default false,
  validation_mode text not null default 'manual' check(validation_mode in ('manual','automatic')),
  tie_breakers jsonb not null default '["major_piece","last_valid_catch_time"]'::jsonb,
  created_at timestamptz not null default now()
);

create table seasons (
  id uuid primary key default gen_random_uuid(),
  club_id uuid not null references clubs(id) on delete cascade,
  name text not null,
  starts_on date not null,
  ends_on date not null,
  status text not null default 'draft' check(status in ('draft','active','closed'))
);

create table competitions (
  id uuid primary key default gen_random_uuid(),
  club_id uuid not null references clubs(id) on delete cascade,
  season_id uuid not null references seasons(id) on delete cascade,
  name text not null,
  starts_at timestamptz,
  ends_at timestamptz,
  ruleset_id uuid references rulesets(id)
);

create table mangas (
  id uuid primary key default gen_random_uuid(),
  club_id uuid not null references clubs(id) on delete cascade,
  competition_id uuid not null references competitions(id) on delete cascade,
  name text not null,
  venue text,
  starts_at timestamptz not null,
  ends_at timestamptz not null,
  ruleset_id uuid references rulesets(id),
  status text not null default 'scheduled' check(status in ('scheduled','active','finished')),
  unique (competition_id, name)
);

create table participants (
  id uuid primary key default gen_random_uuid(),
  club_id uuid not null references clubs(id) on delete cascade,
  external_code text,
  full_name text not null,
  license_number text,
  created_at timestamptz not null default now()
);

create table registrations (
  id uuid primary key default gen_random_uuid(),
  club_id uuid not null references clubs(id) on delete cascade,
  manga_id uuid not null references mangas(id) on delete cascade,
  participant_id uuid not null references participants(id) on delete cascade,
  bib_number text,
  created_at timestamptz not null default now(),
  unique (manga_id, participant_id)
);

create table catches (
  id uuid primary key default gen_random_uuid(),
  club_id uuid not null references clubs(id) on delete cascade,
  manga_id uuid not null references mangas(id) on delete cascade,
  participant_id uuid not null references participants(id) on delete cascade,
  client_uuid uuid not null,
  weight numeric(10,3) not null check(weight > 0),
  length numeric(10,2),
  photo_url text,
  captured_at timestamptz not null,
  notes text,
  validation_status catch_validation_status not null default 'pending',
  sync_status sync_status not null default 'synced',
  local_created_at timestamptz,
  local_updated_at timestamptz,
  created_by uuid references users(id),
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  unique (manga_id, client_uuid)
);

create table standings (
  id uuid primary key default gen_random_uuid(),
  club_id uuid not null references clubs(id) on delete cascade,
  manga_id uuid not null references mangas(id) on delete cascade,
  participant_id uuid not null references participants(id) on delete cascade,
  rank int not null,
  total_weight numeric(12,3) not null default 0,
  major_piece numeric(10,3) not null default 0,
  valid_catches_count int not null default 0,
  computed_at timestamptz not null default now(),
  unique (manga_id, participant_id)
);

create table sync_events (
  id uuid primary key default gen_random_uuid(),
  club_id uuid not null references clubs(id) on delete cascade,
  device_id text not null,
  user_id uuid references users(id),
  operation text not null,
  entity text not null,
  entity_client_uuid uuid,
  status sync_status not null,
  error_message text,
  payload jsonb,
  created_at timestamptz not null default now()
);

create table audit_logs (
  id uuid primary key default gen_random_uuid(),
  club_id uuid not null references clubs(id) on delete cascade,
  actor_user_id uuid references users(id),
  action text not null,
  target_entity text not null,
  target_id uuid,
  metadata jsonb,
  created_at timestamptz not null default now()
);

create index idx_seasons_club on seasons(club_id);
create index idx_competitions_season on competitions(season_id);
create index idx_mangas_competition on mangas(competition_id);
create index idx_registrations_manga on registrations(manga_id);
create index idx_catches_manga_participant on catches(manga_id, participant_id);
create index idx_catches_validation on catches(manga_id, validation_status);
create index idx_sync_events_device on sync_events(device_id, created_at desc);
create index idx_audit_logs_club on audit_logs(club_id, created_at desc);
