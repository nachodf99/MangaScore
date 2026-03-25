import { Controller, Get, Req, UseGuards } from '@nestjs/common';
import { JwtAuthGuard } from '../common/guards/jwt-auth.guard';
import { RolesGuard } from '../common/guards/roles.guard';
import { RulesetsService } from './rulesets.service';

@Controller('rulesets')
@UseGuards(JwtAuthGuard, RolesGuard)
export class RulesetsController {
  constructor(private readonly service: RulesetsService) {}

  @Get()
  list(@Req() req: { user: { clubId: string } }) {
    return this.service.list(req.user.clubId);
  }
}
