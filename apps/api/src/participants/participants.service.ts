import { Injectable } from '@nestjs/common';

@Injectable()
export class ParticipantsService {
  list(clubId: string) {
    return [{ id: 'participants-demo', clubId }];
  }
}
