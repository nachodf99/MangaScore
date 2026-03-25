import { Body, Controller, Post, Req, UseGuards } from '@nestjs/common';
import { JwtAuthGuard } from '../common/guards/jwt-auth.guard';
import { RolesGuard } from '../common/guards/roles.guard';
import { CatchesService } from './catches.service';
import { CreateCatchDto } from './dto/create-catch.dto';

@Controller('catches')
@UseGuards(JwtAuthGuard, RolesGuard)
export class CatchesController {
  constructor(private readonly catchesService: CatchesService) {}

  @Post()
  create(@Body() dto: CreateCatchDto, @Req() req: { user: { clubId: string } }) {
    return this.catchesService.create(dto, req.user.clubId);
  }
}
