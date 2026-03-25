import { Injectable } from '@nestjs/common';

@Injectable()
export class CompetitionsService {
  list(clubId: string) {
    return [{ id: 'competitions-demo', clubId }];
  }
}
