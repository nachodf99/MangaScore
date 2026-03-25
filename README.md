# MangaFish Starter (SaaS competiciones de pesca)

## FASE 1 — Resumen ejecutivo + arquitectura

MangaFish es un SaaS multi-club para gestionar temporadas, competiciones y mangas de pesca con **operativa offline-first real** en embalse. El juez puede registrar capturas sin cobertura, con cola de sync e idempotencia por `client_uuid`.

### Arquitectura general
- **Monorepo npm workspaces**.
- **apps/api**: NestJS + PostgreSQL (Supabase).
- **apps/mobile**: Expo Router + Zustand + SQLite local.
- **apps/web**: React + Vite + Tailwind admin panel.
- **packages/shared**: tipos/esquemas compartidos (Zod).
- **db/**: SQL inicial y seeds.

### Decisiones técnicas justificadas
- **Supabase Auth opcional + JWT backend**: starter usa JWT simple para acelerar MVP, listo para intercambiar proveedor de identidad.
- **Offline-first**: SQLite como source local temporal + `sync_status` (`pending/synced/error`) + sync batch.
- **Idempotencia**: constraint `unique(manga_id, client_uuid)` evita duplicados.
- **Multitenancy por club**: `club_id` en entidades de negocio + guards y filtros por club.

---

## FASE 2 — Estructura monorepo y dependencias

```txt
.
├─ apps/
│  ├─ api/ (NestJS)
│  ├─ mobile/ (Expo RN)
│  └─ web/ (React Vite)
├─ packages/
│  └─ shared/ (tipos/esquemas)
├─ db/
│  ├─ migrations/001_init.sql
│  └─ seeds/demo.sql
└─ .env.example
```

Dependencias clave:
- API: `@nestjs/*`, `typeorm`, `pg`, `class-validator`.
- Mobile: `expo`, `expo-router`, `expo-sqlite`, `zustand`, `react-hook-form`, `zod`.
- Web: `vite`, `react-router-dom`, `tailwindcss`, `react-hook-form`.

---

## FASE 3 — Modelo de datos, SQL, relaciones, constraints e índices

Ver migración completa: `db/migrations/001_init.sql`.

Entidades: `clubs`, `users`, `memberships`, `rulesets`, `seasons`, `competitions`, `mangas`, `participants`, `registrations`, `catches`, `standings`, `sync_events`, `audit_logs`.

Claves del modelo:
- `memberships` define rol por club.
- `mangas.ruleset_id` permite override sobre competición/club.
- `catches` guarda `client_uuid`, `local_created_at`, `local_updated_at`, `sync_status`.
- `standings` materializa ranking recalculado.

Índices incluidos para consultas operativas frecuentes (manga, validación, sincronización y auditoría).

---

## FASE 4 — Backend NestJS

### Módulos
`auth`, `clubs`, `seasons`, `competitions`, `mangas`, `participants`, `catches`, `standings`, `sync`, `rulesets`.

### Auth/autorización
- JWT Bearer (`auth/login` en starter).
- `JwtAuthGuard` + `RolesGuard` + decorador `@Roles(...)`.

### Endpoints REST ejemplo
- `POST /api/auth/login`
- `GET /api/clubs`
- `GET /api/seasons`
- `GET /api/competitions`
- `GET /api/mangas`
- `GET /api/participants`
- `POST /api/catches`
- `GET /api/standings`
- `POST /api/sync/catches`

### DTOs/validaciones
- `CreateCatchDto` valida UUIDs, peso mínimo, fecha ISO y notas.
- `SyncCatchesDto` valida array batch de capturas.

---

## FASE 5 — App móvil Expo

### Estructura
- `app/` rutas Expo Router (`/`, `/mangas`, `/sync`).
- `src/stores` Zustand.
- `src/offline/sqlite.ts` capa local.
- `src/services/api.ts` capa API.

### Estrategia offline-sync
1. Registrar captura en SQLite con `sync_status='pending'`.
2. Detectar conectividad (integrar `expo-network` en siguiente iteración).
3. Enviar batch a `/sync/catches`.
4. Marcar `synced` o `error` local.
5. Reintentos programados y manuales desde pantalla `/sync`.

Conflictos: last-write-wins por `local_updated_at`; duplicados bloqueados por `(manga_id, client_uuid)`.

---

## FASE 6 — Panel web admin

Rutas base implementadas:
- Dashboard
- Temporadas
- Competiciones
- Detalle manga
- Participantes
- Validación de capturas

Base UI con Tailwind y navegación preparada para tablas/formularios.

---

## FASE 7 — Código starter funcional mínimo

Incluye:
- API arrancable con Nest (`apps/api/src/main.ts`).
- Mobile Expo Router (`apps/mobile/app/*`).
- Web admin Vite (`apps/web/src/*`).
- `env.example` raíz.
- SQL y seed demo.

---

## FASE 8 — Arranque local exacto

1. Copiar variables:
```bash
cp .env.example .env
```
2. Instalar:
```bash
npm install
```
3. Levantar PostgreSQL/Supabase local y ejecutar SQL `db/migrations/001_init.sql`.
4. Seed opcional `db/seeds/demo.sql`.
5. Desarrollo paralelo:
```bash
npm run dev
```

### Scripts npm
- `npm run dev`
- `npm run build`
- `npm run typecheck`
- `npm run dev:api`
- `npm run dev:web`
- `npm run dev:mobile`

---

## FASE 9 — Expo + EAS

Configurado en `apps/mobile/app.json` y `apps/mobile/eas.json`.

Perfiles:
- `development`: development client.
- `preview`: Android APK instalable interno.
- `production`: Android AAB para Play Store.

### Comandos build modernos
```bash
cd apps/mobile
npx eas login
npx eas init
npx eas build --platform android --profile preview
npx eas build --platform android --profile production
npx eas build --platform ios --profile production
```

---

## Ranking por defecto (MVP)
- Capturas válidas por peso desc.
- Top N configurable (default 5).
- Suma de pesos top N por participante.
- Pieza mayor global.
- Desempates configurables: `major_piece`, `last_valid_catch_time`, `valid_catches_count`.

---

## Seguridad mínima MVP
- Aislamiento por `club_id`.
- Roles por membership.
- Validación DTO + Zod compartido.
- `audit_logs` y `sync_events` para trazabilidad crítica.

