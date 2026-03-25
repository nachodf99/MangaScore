import { Injectable } from '@nestjs/common';

@Injectable()
export class MangasService {
  list(clubId: string) {
    return [{ id: 'mangas-demo', clubId }];
  }
}
