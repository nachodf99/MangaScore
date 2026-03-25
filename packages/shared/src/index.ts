import { z } from 'zod';

export const RoleSchema = z.enum(['superadmin', 'admin_club', 'juez', 'participante']);
export type Role = z.infer<typeof RoleSchema>;

export const SyncStatusSchema = z.enum(['pending', 'synced', 'error']);
export type SyncStatus = z.infer<typeof SyncStatusSchema>;

export const TieBreakerSchema = z.enum(['major_piece', 'last_valid_catch_time', 'valid_catches_count']);

export const RulesetConfigSchema = z.object({
  topN: z.number().int().min(1).max(20).default(5),
  weightUnit: z.enum(['g', 'kg']).default('g'),
  allowLength: z.boolean().default(true),
  photoRequired: z.boolean().default(false),
  validationMode: z.enum(['manual', 'automatic']).default('manual'),
  tieBreakers: z.array(TieBreakerSchema).default(['major_piece', 'last_valid_catch_time'])
});

export const CatchUpsertSchema = z.object({
  clientUuid: z.string().uuid(),
  mangaId: z.string().uuid(),
  participantId: z.string().uuid(),
  weight: z.number().positive(),
  length: z.number().positive().nullable().optional(),
  capturedAt: z.string().datetime(),
  notes: z.string().max(500).optional(),
  photoPath: z.string().nullable().optional(),
  localCreatedAt: z.string().datetime(),
  localUpdatedAt: z.string().datetime()
});

export type CatchUpsert = z.infer<typeof CatchUpsertSchema>;
