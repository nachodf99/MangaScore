insert into clubs (id, name) values ('11111111-1111-1111-1111-111111111111', 'Club Demo Sevilla');
insert into users (id, email, full_name) values ('21111111-1111-1111-1111-111111111111', 'admin@clubdemo.es', 'Admin Demo');
insert into memberships (club_id, user_id, role) values ('11111111-1111-1111-1111-111111111111','21111111-1111-1111-1111-111111111111','admin_club');
insert into rulesets (id, club_id, name, top_n) values ('31111111-1111-1111-1111-111111111111','11111111-1111-1111-1111-111111111111','Reglamento general',5);
insert into seasons (id, club_id, name, starts_on, ends_on, status) values ('41111111-1111-1111-1111-111111111111','11111111-1111-1111-1111-111111111111','Temporada 2026','2026-01-01','2026-12-31','active');
