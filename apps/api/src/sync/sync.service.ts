import { Injectable } from '@nestjs/common';
import { SyncCatchesDto } from './dto/sync-catches.dto';

@Injectable()
export class SyncService {
  syncBatch(payload: SyncCatchesDto) {
    return {
      processedAt: new Date().toISOString(),
      results: payload.items.map((item) => ({ clientUuid: item.clientUuid, status: 'ok', serverId: crypto.randomUUID() }))
    };
  }
}
