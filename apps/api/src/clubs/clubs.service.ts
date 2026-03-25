import { Injectable } from '@nestjs/common';

@Injectable()
export class ClubsService {
  list(clubId: string) {
    return [{ id: 'clubs-demo', clubId }];
  }
}
