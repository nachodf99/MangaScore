import { Controller, Get, Req, UseGuards } from '@nestjs/common';
import { JwtAuthGuard } from '../common/guards/jwt-auth.guard';
import { RolesGuard } from '../common/guards/roles.guard';
import { CompetitionsService } from './competitions.service';

@Controller('competitions')
@UseGuards(JwtAuthGuard, RolesGuard)
export class CompetitionsController {
  constructor(private readonly service: CompetitionsService) {}

  @Get()
  list(@Req() req: { user: { clubId: string } }) {
    return this.service.list(req.user.clubId);
  }
}
