import { Controller, Get, Req, UseGuards } from '@nestjs/common';
import { JwtAuthGuard } from '../common/guards/jwt-auth.guard';
import { RolesGuard } from '../common/guards/roles.guard';
import { ParticipantsService } from './participants.service';

@Controller('participants')
@UseGuards(JwtAuthGuard, RolesGuard)
export class ParticipantsController {
  constructor(private readonly service: ParticipantsService) {}

  @Get()
  list(@Req() req: { user: { clubId: string } }) {
    return this.service.list(req.user.clubId);
  }
}
