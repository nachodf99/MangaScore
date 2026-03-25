import { Injectable } from '@nestjs/common';

@Injectable()
export class RulesetsService {
  list(clubId: string) {
    return [{ id: 'rulesets-demo', clubId }];
  }
}
