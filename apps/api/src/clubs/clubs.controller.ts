import { Controller, Get, Req, UseGuards } from '@nestjs/common';
import { JwtAuthGuard } from '../common/guards/jwt-auth.guard';
import { RolesGuard } from '../common/guards/roles.guard';
import { ClubsService } from './clubs.service';

@Controller('clubs')
@UseGuards(JwtAuthGuard, RolesGuard)
export class ClubsController {
  constructor(private readonly service: ClubsService) {}

  @Get()
  list(@Req() req: { user: { clubId: string } }) {
    return this.service.list(req.user.clubId);
  }
}
