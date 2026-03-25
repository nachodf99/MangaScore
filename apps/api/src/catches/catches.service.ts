import { Injectable } from '@nestjs/common';
import { CreateCatchDto } from './dto/create-catch.dto';

@Injectable()
export class CatchesService {
  create(dto: CreateCatchDto, clubId: string) {
    return { id: crypto.randomUUID(), clubId, syncStatus: 'synced', ...dto };
  }
}
