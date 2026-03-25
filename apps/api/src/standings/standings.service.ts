import { Injectable } from '@nestjs/common';

@Injectable()
export class StandingsService {
  list(clubId: string) {
    return [{ id: 'standings-demo', clubId }];
  }
}
