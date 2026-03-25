import { Controller, Get, Req, UseGuards } from '@nestjs/common';
import { JwtAuthGuard } from '../common/guards/jwt-auth.guard';
import { RolesGuard } from '../common/guards/roles.guard';
import { StandingsService } from './standings.service';

@Controller('standings')
@UseGuards(JwtAuthGuard, RolesGuard)
export class StandingsController {
  constructor(private readonly service: StandingsService) {}

  @Get()
  list(@Req() req: { user: { clubId: string } }) {
    return this.service.list(req.user.clubId);
  }
}
