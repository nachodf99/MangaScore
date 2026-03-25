import { Injectable } from '@nestjs/common';

@Injectable()
export class SeasonsService {
  list(clubId: string) {
    return [{ id: 'seasons-demo', clubId }];
  }
}
